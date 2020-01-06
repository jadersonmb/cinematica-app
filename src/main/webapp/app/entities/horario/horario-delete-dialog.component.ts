import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHorario } from 'app/shared/model/horario.model';
import { HorarioService } from './horario.service';

@Component({
  templateUrl: './horario-delete-dialog.component.html'
})
export class HorarioDeleteDialogComponent {
  horario?: IHorario;

  constructor(protected horarioService: HorarioService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.horarioService.delete(id).subscribe(() => {
      this.eventManager.broadcast('horarioListModification');
      this.activeModal.close();
    });
  }
}
