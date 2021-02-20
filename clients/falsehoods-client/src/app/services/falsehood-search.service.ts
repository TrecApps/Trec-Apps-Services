import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Falsehood, FalsehoodSearchObject, FullFalsehood } from '../models/falsehoods';
import { FullPublicFalsehood, PublicFalsehood, SearchPublicFalsehood } from '../models/publicFalsehood';

@Injectable({
  providedIn: 'root'
})
export class FalsehoodSearchService {

  constructor(private httpClient: HttpClient) { }

  async searchFalsehoods(searchObj: FalsehoodSearchObject): Promise<Falsehood[]> {
    
    let ret: Falsehood[];
    await this.httpClient.post(environment.FALSEHOOD_SEARCH + "Falsehoods/Media/searchConfirmed", searchObj).toPromise()
      .then((falsehoods: Falsehood[]) => {
        ret = falsehoods;
      }).catch((reason)=> {
        alert(reason);
      });
    return ret;
  }

  async searchRejectedFalsehoods(searchObj: FalsehoodSearchObject): Promise<Falsehood[]> {
    
    let ret: Falsehood[];
    await this.httpClient.post(environment.FALSEHOOD_SEARCH + "Falsehoods/Media/searchRejected", searchObj).toPromise()
      .then((falsehoods: Falsehood[]) => {
        ret = falsehoods;
      }).catch((reason)=> {
        alert(reason);
      });
    return ret;
  }

  async searchSubmittedFalsehoods(size:number, page: number) {
    let ret: Falsehood[];
    await this.httpClient.get(environment.FALSEHOOD_SEARCH + `Falsehoods/Media/searchSubmitted?size=${size}&page=${page}`).toPromise()
      .then((falsehoods: Falsehood[]) => {
        ret = falsehoods;
      }).catch((reason)=> {
        alert(reason);
      });
    return ret;
  }

  async searchPublicFalsehoods(searchObj: SearchPublicFalsehood): Promise<PublicFalsehood[]> {
    
    let ret: PublicFalsehood[];
    await this.httpClient.post(environment.FALSEHOOD_SEARCH + "Falsehoods/Public/searchConfirmed", searchObj).toPromise()
      .then((falsehoods: PublicFalsehood[]) => {
        ret = falsehoods;
      }).catch((reason)=> {
        alert(reason);
      });
    return ret;
  }

  async searchRejectedPublicFalsehoods(searchObj: SearchPublicFalsehood): Promise<PublicFalsehood[]> {
    
    let ret: PublicFalsehood[];
    await this.httpClient.post(environment.FALSEHOOD_SEARCH + "Falsehoods/Public/searchRejected", searchObj).toPromise()
      .then((falsehoods: PublicFalsehood[]) => {
        ret = falsehoods;
      }).catch((reason)=> {
        alert(reason);
      });
    return ret;
  }

  async searchSubmittedPublicFalsehoods(size:number, page: number) {
    let ret: PublicFalsehood[];
    await this.httpClient.get(environment.FALSEHOOD_SEARCH + `Falsehoods/Public/searchSubmitted?size=${size}&page=${page}`).toPromise()
      .then((falsehoods: PublicFalsehood[]) => {
        ret = falsehoods;
      }).catch((reason)=> {
        alert(reason);
      });
    return ret;
  }

  async getFalsehood(id: Number) : Promise<FullFalsehood> {
    let ret: FullFalsehood;
    await this.httpClient.get(environment.FALSEHOOD_SEARCH + `Falsehoods/Media/id/${id}`).
    toPromise().then((f: FullFalsehood) => {
      ret = f;
    }).catch((reason) => {
      alert(reason.message || reason.error.message);
    });

    return ret;
  }

  
  async getPublicFalsehood(id: Number) : Promise<FullPublicFalsehood> {
    let ret: FullPublicFalsehood;
    await this.httpClient.get(environment.FALSEHOOD_SEARCH + `Falsehoods/Public/id/${id}`).
    toPromise().then((f: FullPublicFalsehood) => {
      ret = f;
    }).catch((reason) => {
      alert(reason.message || reason.error.message);
    });

    return ret;
  }
}
