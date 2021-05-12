import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { FullFalsehood } from '../models/falsehoods';
import { FullPublicFalsehood } from '../models/publicFalsehood';

@Injectable({
  providedIn: 'root'
})
export class SubmitService {
  constructor(private httpClient: HttpClient) {
  }


  async submitPublicFalsehood(falsehood: FullPublicFalsehood) : Promise<boolean> {
    let ret: boolean;

    await this.httpClient.post(environment.FALSEHOOD_WRITE + "Update/PublicFalsehood/Insert", falsehood).toPromise().then(()=> ret = true).catch((reason) => {
      ret = false;
      alert(reason.error.message || reason.message);
    });
    return ret;
  }

  async submitFalsehood(falsehood: FullFalsehood) : Promise<boolean> {
    let ret: boolean;

    await this.httpClient.post(environment.FALSEHOOD_WRITE + "Update/Falsehood/Insert", falsehood).toPromise().then(()=> ret = true).catch((reason) => {
      ret = false;
      alert(reason.error.message || reason.message);
    });
    return ret;
  }
}
