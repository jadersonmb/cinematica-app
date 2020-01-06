import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProfissao } from 'app/shared/model/profissao.model';
import { ProfissaoService } from './profissao.service';

@Component({
  templateUrl: './profissao-delete-dialog.component.html'
})
export class ProfissaoDeleteDialogComponent {
  profissao?: IProfissao;

  constructor(protected profissaoService: ProfissaoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.profissaoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('profissaoListModification');
      this.activeModal.close();
    });
  }
}
