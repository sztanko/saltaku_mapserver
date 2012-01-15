package com.saltaku.tileserver.providers.basemaps.storage.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.saltaku.tileserver.providers.basemaps.storage.BasemapStorage;
import com.saltaku.tileserver.providers.basemaps.storage.BasemapStorageException;

public class LuceneBasemapStorage implements BasemapStorage {

	private Directory index;// = FSDirectory.getDirectory("/tmp/ourtestindex/");
	private IndexWriter w;
	private Analyzer analyzer;
	private IndexSearcher searcher;
	private IndexReader r;
	private final int maxWritesBeforeCommit=2000;
	private AtomicInteger writecount=new AtomicInteger(0);
	private Logger log=Logger.getLogger("lucene");
	private ConcurrentHashMap<String, byte[]> cache= new ConcurrentHashMap<String,byte[]>();
	private ReentrantReadWriteLock lock;
	
	private Term shapeTerm=new Term("shape_id");
	private Term keyTerm=new Term("key");
	private int cacheLookups=0;
	private int misses=0;
	private int hits=0;
	private int commits=0;
	
	@Inject
	public LuceneBasemapStorage(@Named("lucenePath") String path) throws BasemapStorageException {
		try {
			this.index=FSDirectory.open(new File(path));
			this.analyzer= new SimpleAnalyzer(Version.LUCENE_34);
			this.w = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34, this.analyzer));
			this.w.commit();
			this.r=IndexReader.open(this.w,false);
			this.searcher=new IndexSearcher(this.r);
			this.lock=new ReentrantReadWriteLock();
		} catch (IOException e) {
			throw new BasemapStorageException(e);
		}
	}
	
	public byte[] get(String shapeId, String key) throws BasemapStorageException {
		//String query="shape_id: \""+shapeId+"\" AND key:\""+key+"\"";
		byte[] out=null;
		
		try {
			BooleanQuery q=new BooleanQuery();
				q.add(new TermQuery(shapeTerm.createTerm(shapeId)),Occur.MUST);
				q.add(new TermQuery(keyTerm.createTerm(key)),Occur.MUST);
			//this.log.info("Search query is: "+q.toString());	
			
			//new MultiFieldQueryParser(Version.LUCENE_34, fields, this.analyzer);
			this.lock.readLock().lock();
			TopDocs tDocs=this.searcher.search(q, 1);
			if(tDocs.scoreDocs.length>0){
				out= this.searcher.doc(tDocs.scoreDocs[0].doc).getBinaryValue("data");
				this.hits++;
			}
			else{ 
			//log.info("could not find anything with "+shapeId+", "+key+", looking in the cache");
			String k=shapeId+"||"+key;
			if(this.cache.containsKey(k))
				{
					out=this.cache.get(k);
					this.cacheLookups++;
				}
			else
			{
				this.misses++;
			//	System.out.println("Hits: "+this.hits+", misses: "+this.misses+", cache hits: "+this.cacheLookups+", commits: "+this.commits);
			}
			
			}
			//
			//if(tDocs.totalHits
		}  catch (IOException e) {
			throw new BasemapStorageException(e);
		}
		finally{
			this.lock.readLock().unlock();
		}
		
		return out;
	}

	public void put(String shapeId, String key, byte[] data) throws BasemapStorageException {
		Document doc = new Document();
		doc.add(new Field("shape_id",shapeId,Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("key",key,Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("data",data));
		
			try {
				w.addDocument(doc);
			} catch (CorruptIndexException e1) {
				throw new BasemapStorageException(e1);
			} catch (IOException e1) {
				throw new BasemapStorageException(e1);
			}
			String k=shapeId+"||"+key;
			this.cache.put(k, data);
			
			if(this.writecount.addAndGet(1)>=this.maxWritesBeforeCommit)
			{
				this.lock.writeLock().lock();
				try{
				this.writecount.set(0);
				w.commit();
				this.cache.clear();
				this.commits++;
				System.out.println("Cache cleared");
				IndexReader oldReader=this.searcher.getIndexReader();
			
				IndexSearcher oldSearcher=this.searcher;
				this.r=IndexReader.open(this.w,false);
				this.searcher=new IndexSearcher(this.r);
				oldReader.close();
				oldSearcher.close();
			} catch (IOException e) {
				throw new BasemapStorageException(e);
			}
				
			finally{
				this.lock.writeLock().unlock();
				
			}
			
				
			
			}
			//this.searcher.getIndexReader().reopen();
		
	}
	
	
	public void shutdown() throws BasemapStorageException {
			try {
				w.close();
				this.index.close();
			} catch (CorruptIndexException e) {
				throw new BasemapStorageException(e);
			} catch (IOException e) {
				throw new BasemapStorageException(e);
			}
		
	}

	public void empty(String shapeId) throws BasemapStorageException {
		//not implemented
	}

}
