import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { TrecAccount } from '../models/TrecAccount';
import { NewUser } from '../models/NewUser';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {

  loginPage: boolean;
  validatePage: boolean;

  account: TrecAccount;

  mode: number;

  constructor(private httpClient: HttpClient) { 
    this.loginPage = true;
    this.validatePage = false;

    this.mode = 2;
  }

  setMode(mode:number) {
    if(mode > 0 && mode < 5) {
      this.mode = mode;
    }
  }

  revertToLogin(reason: any) {
    if(reason.status == 401 || reason.status == 403) {
      this.mode = 2;
    }
  }

  async login(username: String, password: String): Promise<boolean> {
    let ret: boolean = false;

    await this.httpClient.post(`${environment.SERVICE_URL}LogIn`, {username, password})
      .toPromise()
      .then(async () => {
        await this.httpClient.get(`${environment.SERVICE_URL}Account`).toPromise()
        .then((acc: TrecAccount) => {
          this.account = acc;
          this.loginPage = false;
          ret = true;
        });
      });
    return ret;
  }

  async createUser(newUser: NewUser): Promise<boolean> {
    let ret: boolean = false;

    await this.httpClient.post(`${environment.SERVICE_URL}CreateUser`, newUser)
      .toPromise()
      .then(async () => {
        await this.httpClient.get(`${environment.SERVICE_URL}Account`).toPromise()
        .then((acc: TrecAccount) => {
          this.account = acc;
          this.loginPage = this.validatePage = true;
          ret = true;
          this.mode = 4;
        });
      });
    return ret;
  }

  async userExists(username:String): Promise<boolean> {
    let ret: boolean = true;

    await this.httpClient.get(`${environment.SERVICE_URL}CreateUser?username=${username}`)
      .toPromise()
      .then((res: boolean) => {
       ret = res;
      });
    return ret;
  }

  async prepValidation(): Promise<boolean> {
    let ret: boolean = true;

    await this.httpClient.get(`${environment.SERVICE_URL}Validate`)
      .toPromise()
      .then((res: boolean) => {
       ret = res;
      }).catch((reason) => {
        this.revertToLogin(reason);
      });
    return ret;
  }
  
  async attemptValidation(token: string): Promise<String> {
    let ret:String;

    await this.httpClient.post(`${environment.SERVICE_URL}Validate`, null, {
      headers: new HttpHeaders({Verification: token})}).toPromise()
      .catch((reason) => {
        ret = reason.message || reason.error.message;
        this.revertToLogin(reason);
      });
    return ret;
  }

  updateUser() {
    this.httpClient.put(`${environment.SERVICE_URL}UpdateUser`, this.account).toPromise()
      .then(() => {
        alert("Account Updated!");
      })
      .catch((reason) => {
        alert(reason.message || reason.error.message);
        this.revertToLogin(reason);
      });
  }

  updatePassword(username: String, oldPassword: String, newPassword: String) {
    this.httpClient.post(`${environment.SERVICE_URL}UpdatePassword`, {username, oldPassword, newPassword},
    { headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'})}).toPromise()
    .then((res : boolean) => {
      if (res) {
        alert("Successfully Updated Password!");
      } else {
        alert("Failed to Update Password!");
      }
    })
    .catch((reason) => {
      alert(reason.message || reason.error.message);
      this.revertToLogin(reason);
    })
  }
}
