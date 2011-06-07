#!/bin/sh
current_time=`date +%b\ %d\ %H:%M`;
tail -1 /var/log/messages |grep -iE "$current_time";
