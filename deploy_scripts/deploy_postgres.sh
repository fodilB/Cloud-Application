#! /bin/bash
createdb   -U dzTeam  cambridge
psql   -U dzTeam -d  cambridge -f sqlfile.sql
