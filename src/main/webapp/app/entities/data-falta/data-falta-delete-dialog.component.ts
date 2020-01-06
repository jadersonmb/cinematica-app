import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDataFalta } from 'app/shared/model/data-falta.model';
import { DataFaltaService } from './data-falta.service';

@Component({
  templateUrl: './data-falta-delete-dialog.component.html'
})
export class DataFaltaDeleteDialogComponent {
  dataFalta?: IDataFalta;

  constructor(protected dataFaltaService: DataFaltaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dataFaltaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('dataFaltaListModification');
      this.activeModal.close();
    });
  }
}
