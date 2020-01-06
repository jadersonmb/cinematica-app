import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataFalta } from 'app/shared/model/data-falta.model';
import { DataFaltaService } from './data-falta.service';
import { DataFaltaDeleteDialogComponent } from './data-falta-delete-dialog.component';

@Component({
  selector: 'jhi-data-falta',
  templateUrl: './data-falta.component.html'
})
export class DataFaltaComponent implements OnInit, OnDestroy {
  dataFaltas?: IDataFalta[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected dataFaltaService: DataFaltaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.dataFaltaService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IDataFalta[]>) => (this.dataFaltas = res.body ? res.body : []));
      return;
    }
    this.dataFaltaService.query().subscribe((res: HttpResponse<IDataFalta[]>) => {
      this.dataFaltas = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDataFaltas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDataFalta): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDataFaltas(): void {
    this.eventSubscriber = this.eventManager.subscribe('dataFaltaListModification', () => this.loadAll());
  }

  delete(dataFalta: IDataFalta): void {
    const modalRef = this.modalService.open(DataFaltaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dataFalta = dataFalta;
  }
}
