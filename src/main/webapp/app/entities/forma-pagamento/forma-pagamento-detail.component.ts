import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormaPagamento } from 'app/shared/model/forma-pagamento.model';

@Component({
  selector: 'jhi-forma-pagamento-detail',
  templateUrl: './forma-pagamento-detail.component.html'
})
export class FormaPagamentoDetailComponent implements OnInit {
  formaPagamento: IFormaPagamento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formaPagamento }) => {
      this.formaPagamento = formaPagamento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
