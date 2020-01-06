import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';

@Component({
  selector: 'jhi-configuracao-agenda-detail',
  templateUrl: './configuracao-agenda-detail.component.html'
})
export class ConfiguracaoAgendaDetailComponent implements OnInit {
  configuracaoAgenda: IConfiguracaoAgenda | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configuracaoAgenda }) => {
      this.configuracaoAgenda = configuracaoAgenda;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
