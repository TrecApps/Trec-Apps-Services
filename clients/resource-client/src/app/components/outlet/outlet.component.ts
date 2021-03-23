import { Component, OnInit } from '@angular/core';
import { MediaOutlet, MediaOutletEntry } from 'src/app/models/mediaOutlet';
import { SearchService } from 'src/app/services/search.service';
import { UpdateService } from 'src/app/services/update.service';

@Component({
  selector: 'app-outlet',
  templateUrl: './outlet.component.html',
  styleUrls: ['./outlet.component.css']
})
export class OutletComponent implements OnInit {

  mainOutlet: MediaOutletEntry;

  searchOutlet: MediaOutlet[];
  editMode: boolean;
  searchText:String;
  constructor(private search: SearchService, private update: UpdateService) {
    this.mainOutlet = null;
    this.searchOutlet = [];
    this.editMode = false;
    this.searchText = "";
   }

  ngOnInit(): void {
  }

  updateOut() {
    if(!this.mainOutlet){
      return;
    }

    this.update.updateMediaOutlet(this.mainOutlet);
    this.mainOutlet = null;
  }

  async onSearchUpdate(event:any){
    let p = this.search.searchMediaOutlets(event.target.value)
    p.then((outlets: MediaOutlet[])=> {
      this.searchOutlet = outlets;
    });
  }

  initializeNew() {
    let reg = new MediaOutletEntry();
    reg.text = "";
    reg.outlet = new MediaOutlet();
    reg.outlet.outletId = null;
    this.editMode = false;
    
    this.mainOutlet = reg;
  }

  async getOutlet(id: Number) {
    let p = this.search.getMediaOutlet(id);
    p.then((outlet: MediaOutletEntry)=> {
      this.mainOutlet = outlet;
      this.editMode = false;
    });

    this.searchText = "";
  }

  edit() {
    this.editMode = true;
  }
}
