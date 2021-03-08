import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticated: boolean;
  constructor(private httpClient: HttpClient) { 
    this.isAuthenticated = false;
  }

  login() {
    this.httpClient.get(environment.RESOURCE_URL+ "search/isAuth",{ responseType: 'text' }).toPromise().then((ret: String) =>{
      this.isAuthenticated = ret.length == 0;
      if(!this.isAuthenticated) {
        console.log("In then portion: about to encode URL");
        let newUrl = `${environment.AUTH_URL}login?client_id=${ret}&redirect_url=${encodeURIComponent(window.location.href)}`;
        console.log("new Url is " + newUrl);
        window.location.href = newUrl;
      }
    }).catch((reason) => {
      alert(reason.message || reason.error.message);
    });
  }

  logout() {
    this.httpClient.get(environment.AUTH_URL + "logout");
    this.isAuthenticated = false;
  }
}
