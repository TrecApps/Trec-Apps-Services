import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { InstitutionEntry } from '../models/institution';
import { MediaOutletEntry } from '../models/mediaOutlet';
import { PublicFigureEntry } from '../models/publicFigure';
import { RegionEntry } from '../models/region';

@Injectable({
  providedIn: 'root'
})
export class UpdateService {
  constructor(private httpClient: HttpClient) { }

  updateRegion(region: RegionEntry) {
    let promise = (region.region.id == null) ? 
      (this.httpClient.post(environment.RESOURCE_URL + "Update/Region", region)) :
      (this.httpClient.put(environment.RESOURCE_URL + "Update/Region", region));
    
    promise.toPromise().then(() =>{
      alert("Successfully Submitted Region Entry!");
    }).catch((reason)=> {
      alert(reason.message || reason.error.message);
    });
  }

  updateInstitution(inst: InstitutionEntry) {
    let promise = (inst.institution.id == null) ? 
      (this.httpClient.post(environment.RESOURCE_URL + "Update/Institution", inst)) :
      (this.httpClient.put(environment.RESOURCE_URL + "Update/Institution", inst));
    
    promise.toPromise().then(() =>{
      alert("Successfully Submitted Institution Entry!");
    }).catch((reason)=> {
      alert(reason.message || reason.error.message);
    });
  }

  updatePublicFigure(figure: PublicFigureEntry){
    let promise = (figure.figure.id == null) ? 
      (this.httpClient.post(environment.RESOURCE_URL + "Update/PublicFigure", figure)) :
      (this.httpClient.put(environment.RESOURCE_URL + "Update/PublicFigure", figure));
    
    promise.toPromise().then(() =>{
      alert("Successfully Submitted Public Figure Entry!");
    }).catch((reason)=> {
      alert(reason.message || reason.error.message);
    });
  }

  updateMediaOutlet(outlet: MediaOutletEntry) {
    let promise = (outlet.outlet.outletId == null) ? 
      (this.httpClient.post(environment.RESOURCE_URL + "Update/MediaOutlet", outlet)) :
      (this.httpClient.put(environment.RESOURCE_URL + "Update/MediaOutlet", outlet));
    
    promise.toPromise().then(() =>{
      alert("Successfully Submitted Media Outlet Entry!");
    }).catch((reason)=> {
      alert(reason.message || reason.error.message);
    });
  }

}
