import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFormaPagamento } from 'app/shared/model/forma-pagamento.model';
import { FormaPagamentoService } from './forma-pagamento.service';

@Component({
  templateUrl: './forma-pagamento-delete-dialog.component.html'
})
export class FormaPagamentoDeleteDialogComponent {
  formaPagamento?: IFormaPagamento;

  constructor(
    protected formaPagamentoService: FormaPagamentoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formaPagamentoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('formaPagamentoListModification');
      this.activeModal.close();
    });
  }
}
