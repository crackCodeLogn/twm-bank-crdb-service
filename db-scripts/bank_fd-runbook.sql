-- restore from CSV
import into bank_fd
CSV DATA (
	'http://<host>:<port>/FixedDeposits-2022-03-11T235629.csv'
) WITH delimiter = '|';


-- Mark FDs which are past it's end date to expired
update bank_fd set is_active = 'false' where dt_end <= '2022-03-31';


