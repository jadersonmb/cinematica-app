import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFluxoCaixa } from 'app/shared/model/fluxo-caixa.model';
import { FluxoCaixaService } from './fluxo-caixa.service';

@Component({
  templateUrl: './fluxo-caixa-delete-dialog.component.html'
})
export class FluxoCaixaDeleteDialogComponent {
  fluxoCaixa?: IFluxoCaixa;

  constructor(
    protected fluxoCaixaService: FluxoCaixaService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fluxoCaixaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fluxoCaixaListModification');
      this.activeModal.close();
    });
  }
}
