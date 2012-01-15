DROP TABLE IF EXISTS `areas`;
CREATE TABLE IF NOT EXISTS `areas` (
  `area_id` varchar(32) NOT NULL,
  `parent_id` varchar(32) NOT NULL DEFAULT '-1',
  `name` varchar(255) NOT NULL DEFAULT 'unnamed',
  `bbox` geometry DEFAULT NULL,
  `centroid` point DEFAULT NULL,
  `num_items` int(11) NOT NULL DEFAULT '0',
  `area` double NOT NULL DEFAULT '-1',
  `min_area` double NOT NULL DEFAULT '-1',
  `max_area` double NOT NULL DEFAULT '-1',
  `insertTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`area_id`),
  KEY `parent_id` (`parent_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `areas`:
--   `parent_id`
--       `areas` -> `area_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `area_geom`
--
-- Creation: Jan 04, 2012 at 04:53 PM
-- Last update: Jan 04, 2012 at 04:53 PM
-- Last check: Jan 04, 2012 at 04:53 PM
--

DROP TABLE IF EXISTS `area_geom`;
CREATE TABLE IF NOT EXISTS `area_geom` (
  `area_id` varchar(32) NOT NULL,
  `geom_id` int(11) NOT NULL,
  `area_code` varchar(20) NOT NULL,
  `name` varchar(250) NOT NULL,
  `english_name` varchar(255) NOT NULL,
  `area` double NOT NULL,
  `centroid` point NOT NULL,
  `shape` multipolygon NOT NULL,
  `simple_shape` multipolygon DEFAULT NULL,
  `bb` geometry NOT NULL,
  `insertTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`area_id`,`geom_id`),
  UNIQUE KEY `area_code` (`area_code`),
  KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `area_geom`:
--   `area_id`
--       `areas` -> `area_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `area_geom_mappings`
--
-- Creation: Jan 09, 2012 at 09:12 PM
-- Last update: Jan 09, 2012 at 09:12 PM
-- Last check: Jan 09, 2012 at 09:12 PM
--

DROP TABLE IF EXISTS `area_geom_mappings`;
CREATE TABLE IF NOT EXISTS `area_geom_mappings` (
  `child_area_id` varchar(32) NOT NULL,
  `child_geom_id` int(11) NOT NULL,
  `parent_area_id` varchar(32) NOT NULL,
  `parent_geom_id` int(11) NOT NULL,
  `overlap` double NOT NULL,
  PRIMARY KEY (`child_area_id`,`child_geom_id`,`parent_area_id`,`parent_geom_id`),
  KEY `overlap` (`overlap`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `area_geom_mappings`:
--   `child_area_id`
--       `areas` -> `area_id`
--   `child_geom_id`
--       `area_geom` -> `geom_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `area_mappings`
--
-- Creation: Jan 05, 2012 at 10:32 PM
-- Last update: Jan 05, 2012 at 10:32 PM
-- Last check: Jan 05, 2012 at 10:32 PM
--

DROP TABLE IF EXISTS `area_mappings`;
CREATE TABLE IF NOT EXISTS `area_mappings` (
  `child_area_id` varchar(32) NOT NULL,
  `parent_area_id` varchar(32) NOT NULL,
  `mapping` mediumblob NOT NULL,
  PRIMARY KEY (`child_area_id`,`parent_area_id`),
  KEY `parent_area_id` (`parent_area_id`),
  KEY `child_area_id` (`child_area_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `area_mappings`:
--   `child_area_id`
--       `areas` -> `area_id`
--   `parent_area_id`
--       `areas` -> `area_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `correlations`
--
-- Creation: Jan 04, 2012 at 04:55 PM
-- Last update: Jan 04, 2012 at 04:55 PM
-- Last check: Jan 04, 2012 at 04:55 PM
--

DROP TABLE IF EXISTS `correlations`;
CREATE TABLE IF NOT EXISTS `correlations` (
  `dataset1_id` varchar(32) NOT NULL,
  `dataset2_id` varchar(32) NOT NULL,
  `correlation_type` varchar(64) NOT NULL,
  `correlation` double NOT NULL,
  PRIMARY KEY (`dataset1_id`,`dataset2_id`,`correlation_type`),
  KEY `dataset1_id` (`dataset1_id`,`correlation_type`,`correlation`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `correlations`:
--   `dataset1_id`
--       `datasets` -> `dataset_id`
--   `dataset2_id`
--       `datasets` -> `dataset_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `datasets`
--
-- Creation: Jan 04, 2012 at 04:55 PM
-- Last update: Jan 04, 2012 at 06:30 PM
-- Last check: Jan 04, 2012 at 04:55 PM
--

DROP TABLE IF EXISTS `datasets`;
CREATE TABLE IF NOT EXISTS `datasets` (
  `dataset_id` varchar(32) NOT NULL,
  `datasource_id` varchar(64) NOT NULL,
  `name` varchar(255) NOT NULL,
  `date_validity_start` datetime NOT NULL,
  `date_validity_end` datetime NOT NULL,
  `size` int(11) NOT NULL,
  `area_id` varchar(32) NOT NULL,
  `aggregation` varchar(64) NOT NULL,
  `bbox` geometry NOT NULL,
  PRIMARY KEY (`dataset_id`),
  KEY `datasource_id` (`datasource_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `datasets`:
--   `datasource_id`
--       `datasources` -> `datasource_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `dataset_data`
--
-- Creation: Jan 04, 2012 at 04:51 PM
-- Last update: Jan 04, 2012 at 04:51 PM
--

DROP TABLE IF EXISTS `dataset_data`;
CREATE TABLE IF NOT EXISTS `dataset_data` (
  `dataset_id` varchar(32) NOT NULL,
  `area_id` varchar(32) NOT NULL,
  `bbox` geometry NOT NULL,
  `aggregation` varchar(64) NOT NULL,
  `data` mediumblob NOT NULL,
  PRIMARY KEY (`dataset_id`,`area_id`,`aggregation`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `dataset_data`:
--   `dataset_id`
--       `datasets` -> `dataset_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `datasources`
--
-- Creation: Jan 04, 2012 at 05:01 PM
-- Last update: Jan 04, 2012 at 06:29 PM
--

DROP TABLE IF EXISTS `datasources`;
CREATE TABLE IF NOT EXISTS `datasources` (
  `datasource_id` varchar(64) NOT NULL,
  `upload_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `uploader` varchar(255) NOT NULL,
  `is_public` tinyint(1) NOT NULL,
  `geokey_type` enum('ID','GEO','ADDR') NOT NULL,
  `geokey_column_num` int(11) NOT NULL,
  `output_area_id` varchar(32) NOT NULL,
  `aggregators` varchar(50) NOT NULL,
  `separator` varchar(5) NOT NULL,
  `start_line` int(11) NOT NULL,
  PRIMARY KEY (`datasource_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `datasources`:
--   `output_area_id`
--       `areas` -> `area_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `datasource_datasets`
--
-- Creation: Jan 04, 2012 at 04:35 PM
-- Last update: Jan 04, 2012 at 04:35 PM
--

DROP TABLE IF EXISTS `datasource_datasets`;
CREATE TABLE IF NOT EXISTS `datasource_datasets` (
  `datasource_id` varchar(64) NOT NULL,
  `column` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`datasource_id`,`column`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `datasource_datasets`:
--   `datasource_id`
--       `datasources` -> `datasource_id`
--

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--
-- Creation: Jan 04, 2012 at 04:51 PM
-- Last update: Jan 04, 2012 at 04:51 PM
--

DROP TABLE IF EXISTS `tags`;
CREATE TABLE IF NOT EXISTS `tags` (
  `data_id` varchar(32) NOT NULL,
  `data_type` varchar(10) NOT NULL,
  `tag_name` varchar(64) NOT NULL,
  `tag_value` varchar(40000) NOT NULL,
  PRIMARY KEY (`data_id`,`data_type`,`tag_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `workflows`
--
-- Creation: Jan 09, 2012 at 09:18 PM
-- Last update: Jan 09, 2012 at 09:18 PM
--

DROP TABLE IF EXISTS `workflows`;
CREATE TABLE IF NOT EXISTS `workflows` (
  `workflow_id` varchar(64) NOT NULL,
  `session_id` varchar(64) NOT NULL,
  `owner` varchar(64) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `status` enum('RUNNING','FINISHED','FAILED') NOT NULL,
  `progress` double NOT NULL,
  `result` text NOT NULL,
  PRIMARY KEY (`workflow_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

