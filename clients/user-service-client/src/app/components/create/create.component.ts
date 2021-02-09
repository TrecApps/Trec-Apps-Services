import { Component, OnInit } from '@angular/core';
import { NewUser } from 'src/app/models/NewUser';
import { ManagerService } from 'src/app/services/manager.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {

  user: NewUser;

  usernameFree: boolean;
  updateFail:boolean;
  needsFields: boolean;

  constructor(private manager: ManagerService) {
    this.user = new NewUser();
    this.usernameFree = true;
    this.updateFail = this.needsFields = false;
  }

  ngOnInit(): void {
  }

  checkForUserName() {
    this.manager.userExists(this.user.username).then((res: boolean) => {
      this.usernameFree = !res;
    });
  }

  create() {
    if(!this.usernameFree){
      return;
    }
    if(!this.user.validate()) {
      this.needsFields = true;
    } else {
      this.needsFields = false;

      this.manager.createUser(this.user).then((res: boolean) => {
        this.updateFail = !res;
      });
    }
  }

}
