import sys
for line in sys.stdin:
	print line.split(',')[1].strip('"\n')
