import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { TrecAccount } from '../models/TrecAccount';
import { NewUser } from '../models/NewUser';
import { Client, ClientError } from '../models/Client';

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

    await this.httpClient.post(`${environment.SERVICE_URL}users/LogIn`, {username, password})
      .toPromise()
      .then(async () => {
        await this.httpClient.get(`${environment.SERVICE_URL}users/Account`).toPromise()
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

    await this.httpClient.post(`${environment.SERVICE_URL}users/CreateUser`, newUser)
      .toPromise()
      .then(async () => {
        await this.httpClient.get(`${environment.SERVICE_URL}users/Account`).toPromise()
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

    await this.httpClient.get(`${environment.SERVICE_URL}users/UserExists?username=${username}`)
      .toPromise()
      .then((res: boolean) => {
       ret = res;
      });
    return ret;
  }

  async prepValidation(): Promise<boolean> {
    let ret: boolean = true;

    await this.httpClient.get(`${environment.SERVICE_URL}users/Validate`)
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

    await this.httpClient.post(`${environment.SERVICE_URL}users/Validate`, null, {
      headers: new HttpHeaders({Verification: token})}).toPromise()
      .catch((reason) => {
        ret = reason.message || reason.error.message;
        this.revertToLogin(reason);
      }).then(() => {
        this.mode = 3;
      });
    return ret;
  }

  updateUser() {
    this.httpClient.put(`${environment.SERVICE_URL}users/UpdateUser`, this.account).toPromise()
      .then(() => {
        alert("Account Updated!");
      })
      .catch((reason) => {
        alert(reason.message || reason.error.message);
        this.revertToLogin(reason);
      });
  }

  updatePassword(username: String, oldPassword: String, newPassword: String) {


    this.httpClient.post(`${environment.SERVICE_URL}users/UpdatePassword`, {username, oldPassword, newPassword}).toPromise()
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

  async canCreateClient(): Promise<boolean> {
    let ret: boolean;
    await this.httpClient.get(`${environment.SERVICE_URL}clients/canCreate`).toPromise()
      .then((res: boolean) => {
        ret = res;
      }).catch(() => {
        ret = false;
      });
    return ret;
  }

  async createClient(name:String): Promise<ClientError> {
    let ret = new ClientError();
    await this.httpClient.get(`${environment.SERVICE_URL}clients/create?name=${name}`).toPromise()
      .then((res: Client) => {
        ret.client = res;
      }).catch((reason) => {
        if(reason.status) {
          switch(reason.status) {
            case 401:
              this.mode = 2;
              break;
            case 403:
              ret.error = "You are Not Authorized to Create a Trec Client!";
          }
        } else {
          ret.error = "Client Creation Failed! Reason Unknown!";
        }
      });
    return ret;
  }
}
