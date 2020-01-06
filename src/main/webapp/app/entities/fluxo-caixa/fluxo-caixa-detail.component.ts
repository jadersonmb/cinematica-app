import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFluxoCaixa } from 'app/shared/model/fluxo-caixa.model';

@Component({
  selector: 'jhi-fluxo-caixa-detail',
  templateUrl: './fluxo-caixa-detail.component.html'
})
export class FluxoCaixaDetailComponent implements OnInit {
  fluxoCaixa: IFluxoCaixa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fluxoCaixa }) => {
      this.fluxoCaixa = fluxoCaixa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
