#!/usr/bin/env bash
../../_schemacrawler/schemacrawler.sh --server=hsqldb --database=schemacrawler --user=sa --password= --info-level=standard -c template --templating-language=mustache --template "$1"
