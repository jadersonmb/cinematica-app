import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMedico } from 'app/shared/model/medico.model';
import { MedicoService } from './medico.service';

@Component({
  templateUrl: './medico-delete-dialog.component.html'
})
export class MedicoDeleteDialogComponent {
  medico?: IMedico;

  constructor(protected medicoService: MedicoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('medicoListModification');
      this.activeModal.close();
    });
  }
}
