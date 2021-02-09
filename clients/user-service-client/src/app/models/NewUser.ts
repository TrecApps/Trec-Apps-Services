

export class NewUser {
    firstName: String;
	lastName: String;
	username: String;
	mainEmail: String;
	trecEmail: String;
	backupEmail: String;
	password: String;
	birthday: Date;
	
	// Security features
	passwordMonthReset: number;  // How many months before Password needs to be Changed
	timeForValidToken: number;   // How long after login that the token should last (by 10 minutes)
	validTimeFromActivity: number; // Whether apps should update the token every activity
	
	maxLoginAttempts: number; // How many login attempts per hour 
	lockTime: number; // How long to lock the account for 

    validate() : boolean {
        return !!(this.firstName && this.lastName && this.username &&
            this.mainEmail && this.backupEmail && this.password && this.birthday);

    }
}