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
    this.httpClient.get(environment.RESOURCE_URL+ "search/isAuth").toPromise().then((ret: String) =>{
      this.isAuthenticated = ret.length == 0;
      if(!this.isAuthenticated) {
        window.location.href = `${environment.AUTH_URL}login?client_id=${ret}&redirect_url=${encodeURIComponent(window.location.href)}`;
      }
    });
  }

  logout() {
    this.httpClient.get(environment.AUTH_URL + "logout");
    this.isAuthenticated = false;
  }
}
