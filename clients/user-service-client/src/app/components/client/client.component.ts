import { Component, OnInit } from '@angular/core';
import { Client, ClientError } from 'src/app/models/Client';
import { ManagerService } from 'src/app/services/manager.service';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.css']
})
export class ClientComponent implements OnInit {

  clientMessage: String;
  canCreate: boolean;
  clientObj: Client;
  clientName: String;

  constructor(private manager: ManagerService) { 
    this.canCreate = false;
    this.clientObj = null;
    this.clientName = "";
  }

  ngOnInit(): void {
  }

  checkClientPermission() {
    this.manager.canCreateClient().then((res: boolean) => {
      if(res){
        this.canCreate = true;
        this.clientMessage = this.clientName = "";
      } else {
        this.canCreate = false;
        this.clientMessage = "You Do not have permission to create a Trec-Apps Client";
      }
    });
  }

  submitClient() {
    this.manager.createClient(this.clientName).then((value: ClientError)=> {
      this.clientMessage = value.error;
      this.clientObj = value.client;
      this.clientName = "";
      this.canCreate = false;
    });
  }
  
  dropObject() {
    this.canCreate = false;
    this.clientObj = null;
    this.clientName = "";
  }

  moveToUpdate() {
    this.manager.mode = 3;
  }

}
