import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfissao } from 'app/shared/model/profissao.model';

@Component({
  selector: 'jhi-profissao-detail',
  templateUrl: './profissao-detail.component.html'
})
export class ProfissaoDetailComponent implements OnInit {
  profissao: IProfissao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profissao }) => {
      this.profissao = profissao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
