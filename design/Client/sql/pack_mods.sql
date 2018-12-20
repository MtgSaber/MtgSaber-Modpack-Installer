select
	A.Name as 'Mod Name',
	A.PrjURL as 'Mod Project',
	B.FIleName as 'Mod File Name',
	B.FileURL as 'Mod File URL'
from MCMod as A
	join Pack_MCMod as B
		on A.MCModID = B.MCModID
	join (
		select top 1 PackID
		from Pack as D
		where D.Name = ''
	) as C
		on B.PackID = C.PackID