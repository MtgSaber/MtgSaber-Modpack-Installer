select
	A.Dir,
	A.LastUpdated,
	A.ProfileFileName,
	A.ProfileURL,
	A.CfgZipFileName,
	A.CfgZipFileURL,
	B.FileName,
	B.FileURL
from Pack as A
	join ForgeInstFile as B
		on A.ForgeInstFileID = B.ForgeInstFileID
where A.Name = ''