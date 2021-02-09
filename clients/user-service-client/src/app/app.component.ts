import { Component } from '@angular/core';
import { ManagerService } from './services/manager.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'user-service-client';

  manager: ManagerService;

  constructor(manager:ManagerService) {
    this.manager = manager;
  }
}
