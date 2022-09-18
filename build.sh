#!/bin/bash
service mysql start
mysql -e "SOURCE ./init.sql"

exit 127