import { Component, OnInit } from '@angular/core';
import { ManagerService } from 'src/app/services/manager.service';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.css']
})
export class UpdateComponent implements OnInit {

  manager: ManagerService;

  updateMode: boolean;

  

  username: String;
  oldPassword: String;
  newPassword: String;

  constructor(manager: ManagerService) {
    this.manager = manager;
    this.updateMode = true;
  }

  ngOnInit(): void {
  }

  updateAccount() {
    this.manager.updateUser();
  }

  updatePassword() {
    this.manager.updatePassword(this.username, this.oldPassword, this.newPassword);
  }

  toggleUpdateMode() {
    this.updateMode = !this.updateMode;
  }

}
