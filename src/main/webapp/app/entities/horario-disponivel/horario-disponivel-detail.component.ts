import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHorarioDisponivel } from 'app/shared/model/horario-disponivel.model';

@Component({
  selector: 'jhi-horario-disponivel-detail',
  templateUrl: './horario-disponivel-detail.component.html'
})
export class HorarioDisponivelDetailComponent implements OnInit {
  horarioDisponivel: IHorarioDisponivel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horarioDisponivel }) => {
      this.horarioDisponivel = horarioDisponivel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
