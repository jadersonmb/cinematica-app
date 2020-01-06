import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';
import { ConfiguracaoAgendaService } from './configuracao-agenda.service';

@Component({
  templateUrl: './configuracao-agenda-delete-dialog.component.html'
})
export class ConfiguracaoAgendaDeleteDialogComponent {
  configuracaoAgenda?: IConfiguracaoAgenda;

  constructor(
    protected configuracaoAgendaService: ConfiguracaoAgendaService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configuracaoAgendaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('configuracaoAgendaListModification');
      this.activeModal.close();
    });
  }
}
