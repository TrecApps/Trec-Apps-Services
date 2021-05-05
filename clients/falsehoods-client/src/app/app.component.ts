import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'falsehoods-client';

  
  authService: AuthService;

  constructor(authService: AuthService, private route: ActivatedRoute)
  {
    this.authService = authService;
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params=>{
      let code = params['code'];
      console.log(`code=${code}`);
      this.authService.login(false,code);
    });
  }

  login() {
    this.authService.login(true , null);
  }

  logout() {
    this.authService.logout();
  }
}
