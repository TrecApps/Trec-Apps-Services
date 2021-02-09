import { Component, OnInit } from '@angular/core';
import { ManagerService } from 'src/app/services/manager.service';

@Component({
  selector: 'app-validate',
  templateUrl: './validate.component.html',
  styleUrls: ['./validate.component.css']
})
export class ValidateComponent implements OnInit {

  validToken: string;

  constructor(private manager: ManagerService) { }

  ngOnInit(): void {
  }

  getToken() {
    this.manager.prepValidation().then((res:boolean) => {

    });
  }

  postToken() {
    this.manager.attemptValidation(this.validToken).catch((reason) => {
      alert(reason.message || reason.error.message);
    }).then(() => {
      
    });
  }

}
