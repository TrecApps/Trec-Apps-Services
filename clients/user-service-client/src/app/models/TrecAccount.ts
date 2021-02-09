

export class TrecAccount {
    accountId: Number;
    firstName: String;
    lastName: String;
    username: String;
    mainEmail: String;
    // trecEmail;
    backupEmail: String;
    // token;
    birthday: Date;
    isValidated: Number;
    color: String;
    
    // Set Security Attributes
    passwordMonthReset: number;
    passwordChanged: Date;
    timeForValidToken: number;
    validTimeFromActivity: number;
    maxLoginAttempts: number;
    lockTime: number;
}