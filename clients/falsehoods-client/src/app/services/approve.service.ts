import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApproveService {

  private falsehoodMapping:String;
  private publicFalsehoodMapping:String;
  private publicFigureMapping: String;

  constructor(private httpClient: HttpClient) { 
    this.falsehoodMapping = "Update/Falsehood";
    this.publicFalsehoodMapping = "Update/PublicFalsehood";
    this.publicFigureMapping = "PublicFigure";
  }


  async approveFalsehood(app:boolean, id: number, reason: string) {
    await this.httpClient.put(environment.FALSEHOOD_WRITE + `Update/Falsehood/${app ? 'Approve' : 'Reject'}`,
     {falsehood: id, comment: reason}).toPromise().catch((reason)=>{
      alert(reason.message || reason.error.message);
     });
  }

  async approvePublicFalsehood(app:boolean, id: number, reason: string) {
    await this.httpClient.put(environment.FALSEHOOD_WRITE + `Update/Falsehood/${app ? 'Approve' : 'Reject'}`,
     {falsehood: id, comment: reason}).toPromise().catch((reason)=>{
      alert(reason.message || reason.error.message);
     });
  }

}
