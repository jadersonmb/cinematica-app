import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHorarioDisponivel } from 'app/shared/model/horario-disponivel.model';
import { HorarioDisponivelService } from './horario-disponivel.service';

@Component({
  templateUrl: './horario-disponivel-delete-dialog.component.html'
})
export class HorarioDisponivelDeleteDialogComponent {
  horarioDisponivel?: IHorarioDisponivel;

  constructor(
    protected horarioDisponivelService: HorarioDisponivelService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.horarioDisponivelService.delete(id).subscribe(() => {
      this.eventManager.broadcast('horarioDisponivelListModification');
      this.activeModal.close();
    });
  }
}
