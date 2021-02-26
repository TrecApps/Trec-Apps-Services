import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticated: boolean;
  constructor() { 
    this.isAuthenticated = false;
  }

  login() {

  }

  logout() {
    
  }
}
