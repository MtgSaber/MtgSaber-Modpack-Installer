select
	A.Name as 'Pack Name',
	A.Ver as 'Pack Version',
	C.Ver as 'MC Version',
	B.Ver as 'Forge Version',
	D.ModCnt as 'Mods',
	E.UsrCnt as 'Users'
from Pack as A
	join ForgeInstFile as B
		on A.ForgeInstFileID = B.ForgeInstFileID
	join MCVersion as C
		on B.MCVersionID = C.MCVersionID
	join (
		select F.PackID, count(F.PackID) as ModCnt
		from Pack_MCMod as F
		group by F.PackID
	) as D
		on A.PackID = D.PackID
	join (
		select G.PackID, count(G.PackID) as UsrCnt
		from Usr_Pack as G
		group by G.PackID
	) as E
		on A.PackID = E.PackID