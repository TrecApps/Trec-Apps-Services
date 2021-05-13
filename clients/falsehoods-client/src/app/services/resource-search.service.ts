import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Institution, InstitutionEntry } from '../models/institution';
import { MediaOutlet, MediaOutletEntry } from '../models/mediaOutlet';
import { PublicFigure, PublicFigureEntry } from '../models/publicFigure';
import { Region, RegionEntry } from '../models/region';


@Injectable({
  providedIn: 'root'
})
export class ResourceSearchService {

  constructor(private httpClient: HttpClient) { }

  async searchRegions(searchTerm: String): Promise<Region[]> {
    let ret: Region[];

    await this.httpClient.get(environment.RESOURCE_URL + `regions/${searchTerm.replace(' ', '_')}`)
      .toPromise().then((regions: Region[]) => {
        ret = regions;
      }).catch((reason)=> {
        alert(reason);
      });
      return ret;
  }

  async getRegion(id: Number): Promise<RegionEntry> {
    let ret: RegionEntry;
    await this.httpClient.get(environment.RESOURCE_URL + `region/${id}`)
    .toPromise().then((region: RegionEntry) => {
      ret = region;
    }).catch((reason)=> {
      alert(reason);
    });

    return ret;
  }

  async searchInstitutions(searchTerm: String): Promise<Institution[]> {
    let ret: Institution[];

    await this.httpClient.get(environment.RESOURCE_URL + `institutions/${searchTerm.replace(' ', '_')}`)
      .toPromise().then((inst: Institution[]) => {
        ret = inst;
      }).catch((reason)=> {
        alert(reason.message || reason.error.message);
      });
      return ret;
  }

  async getInstitution(id: Number): Promise<InstitutionEntry> {
    let ret: InstitutionEntry;
    await this.httpClient.get(environment.RESOURCE_URL + `institution/${id}`)
    .toPromise().then((inst: InstitutionEntry) => {
      ret = inst;
    }).catch((reason)=> {
      alert(reason.message || reason.error.message);
    });

    return ret;
  }

  async searchPublicFigures(searchTerm: String): Promise<PublicFigure[]> {
    let ret: PublicFigure[];

    await this.httpClient.get(environment.RESOURCE_URL + `publicFigures/${searchTerm.replace(' ', '_')}`)
    .toPromise().then((figures: PublicFigure[]) => {
      ret = figures;
    }).catch((reason)=> {
      alert(reason);
    });

    return ret;
  }

  async getPublicFigure(id: Number): Promise<PublicFigureEntry> {
    let ret: PublicFigureEntry

    await this.httpClient.get(environment.RESOURCE_URL + `publicFigure/${id}`)
    .toPromise().then((figures: PublicFigureEntry) => {
      ret = figures;
    }).catch((reason)=> {
      alert(reason);
    });

    return ret;
  }

  async searchMediaOutlets(searchTerm: String): Promise<MediaOutlet[]> {
    let ret: MediaOutlet[];

    await this.httpClient.get(environment.RESOURCE_URL + `mediaOutlets/${searchTerm.replace(' ', '_')}`)
    .toPromise().then((outlets: MediaOutlet[]) => {
      ret = outlets;
    }).catch((reason)=> {
      alert(reason);
    });

    return ret;
  }

  async getMediaOutlet(id: Number): Promise<MediaOutletEntry> {
    let ret: MediaOutletEntry

    await this.httpClient.get(environment.RESOURCE_URL + `mediaOutlet/${id}`)
    .toPromise().then((outlet: MediaOutletEntry) => {
      ret = outlet;
    }).catch((reason)=> {
      alert(reason);
    });

    return ret;
  }
}
