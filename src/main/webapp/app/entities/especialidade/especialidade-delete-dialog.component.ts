import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEspecialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from './especialidade.service';

@Component({
  templateUrl: './especialidade-delete-dialog.component.html'
})
export class EspecialidadeDeleteDialogComponent {
  especialidade?: IEspecialidade;

  constructor(
    protected especialidadeService: EspecialidadeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.especialidadeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('especialidadeListModification');
      this.activeModal.close();
    });
  }
}
