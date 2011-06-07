#!/usr/bin/perl
system("clear");
my $current_time=`date +%b\\ %d\\ %H:%M`;
print $current_time;
my @log=`tail -20 /var/log/messages|grep -i "$current_time"`;
if(!@log){
	write;
}
foreach $line(@log){
	@campos=split('\|',$line);
	#if($campos[1]){
	write;
	#}
}

format STDOUT_TOP=
===================================================================================================================
| SOURCE IP ADDR  |  DEST IP ADDR    |  SOURCE PORT    |     DEST PORT    |     SOURCE MAC     |     DEST MAC     |
===================================================================================================================
.
format STDOUT =
|@<<<<<<<<<<<<<<< | @<<<<<<<<<<<<<<< | @|||||||||||||| | @||||||||||||||| | @<<<<<<<<<<<<<<<<< | @<<<<<<<<<<<<<<<<|
$campos[1], $campos[2],$campos[3], $campos[4],$campos[5], $campos[6]
.
