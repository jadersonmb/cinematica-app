import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAgenda } from 'app/shared/model/agenda.model';
import { AgendaService } from './agenda.service';

@Component({
  templateUrl: './agenda-delete-dialog.component.html'
})
export class AgendaDeleteDialogComponent {
  agenda?: IAgenda;

  constructor(protected agendaService: AgendaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agendaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('agendaListModification');
      this.activeModal.close();
    });
  }
}
