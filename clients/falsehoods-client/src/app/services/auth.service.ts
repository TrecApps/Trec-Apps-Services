import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticated: boolean;
  refreshToken:String;
  credit = 0;
  constructor(private httpClient: HttpClient) { 
    this.isAuthenticated = false;
  }

  login(needsAuth: boolean, code:String) {

    if(code || this.refreshToken){
      this.httpClient.post(environment.FALSEHOOD_WRITE + "tokenize", code || this.refreshToken).toPromise().then((str: String) =>{
        this.refreshToken = str;
        this.isAuthenticated = ((str != null) && (str != undefined));
        console.log("Authenticated!");
      }).catch((reason) =>{
        console.log("Not Authenticated!", reason.message || reason.error.message);
        this.refreshToken = null;
        this.isAuthenticated = false;
      });
    }else {

      this.httpClient.get(environment.FALSEHOOD_WRITE+ "isAuth",{ responseType: 'text' }).toPromise().then((ret: String) =>{
        this.isAuthenticated = (ret.length == 0);
        if(!this.isAuthenticated && needsAuth) {
          console.log("In then portion: about to encode URL");
          let newUrl = `${environment.AUTH_URL}login?client_id=${ret}&redirect_url=${encodeURIComponent(window.location.href)}`;
          console.log("new Url is " + newUrl);
          window.location.href = newUrl;
        }
      }).catch((reason) => {
        alert(reason.message || reason.error.message);
      });
    }
  }

  logout() {
    this.httpClient.get(environment.AUTH_URL + "logout");
    this.isAuthenticated = false;
  }
}
