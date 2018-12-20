create table MCMod (
	MCModID int identity(0,1) primary key,
	Name varchar(50) not null unique,
	PrjURL varchar(255)
);

create table MCVersion (
	MCVersionID int identity(0,1) primary key,
	Ver varchar(20) not null unique
);

create table ForgeInstFile (
	ForgeInstFileID int identity(0,1) primary key,
	MCVersionID int not null foreign key references MCVersion(MCVersionID),
	Ver varchar(50) not null unique,
	FileName varchar(128) not null unique,
	FileURL varchar(255) not null unique
);

create table Pack (
	PackID int identity(0,1) primary key,
	ForgeInstFileID int not null foreign key references ForgeInstFile(ForgeInstFileID),
	Name varchar(50) not null unique,
	Ver varchar(10) not null,
	LastUpdated DateTime not null,
	CfgZipFileName varchar(128) not null unique,
	CfgZipFileURL varchar(255) not null unique,
	Dir varchar(15) not null unique,
	ProfileFileName varchar(128) not null unique,
	ProfileURL varchar(255) not null unique
);

create table Pack_MCMod (
	PackID int not null foreign key references Pack(PackID),
	MCModID int not null foreign key references MCMod(MCModID),
	FileName varchar(128) not null,
	FileURL varchar(255) not null
);

create table Usr (
	UsrID int identity(0,1) primary key,
	Name varchar(30) not null unique,
);

create table Usr_Pack (
	UsrID int not null foreign key references Usr(UsrID),
	PackID int not null foreign key references Pack(PackID)
);

create table MCServer (
	MCServerID int identity(0,1) primary key,
	Name varchar(30) not null unique,
	Address varchar(50) not null
);