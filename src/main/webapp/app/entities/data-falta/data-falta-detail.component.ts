import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDataFalta } from 'app/shared/model/data-falta.model';

@Component({
  selector: 'jhi-data-falta-detail',
  templateUrl: './data-falta-detail.component.html'
})
export class DataFaltaDetailComponent implements OnInit {
  dataFalta: IDataFalta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataFalta }) => {
      this.dataFalta = dataFalta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
