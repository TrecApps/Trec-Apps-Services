import { Component, OnInit } from '@angular/core';
import { ManagerService } from 'src/app/services/manager.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;
  loginFail: boolean;

  constructor(private manager: ManagerService) {
    this.loginFail = false;
   }

  ngOnInit(): void {
  }

  login() {
    this.manager.login(this.username, this.password).then((res: boolean) => {
      if(!res) {
        this.loginFail = true;
      } else {
        this.manager.setMode(3);
      }
    });
  }

  moveToCreate() {
    this.manager.setMode(1);
  }

}
