import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHorarioDisponivel } from 'app/shared/model/horario-disponivel.model';
import { HorarioDisponivelService } from './horario-disponivel.service';
import { HorarioDisponivelDeleteDialogComponent } from './horario-disponivel-delete-dialog.component';

@Component({
  selector: 'jhi-horario-disponivel',
  templateUrl: './horario-disponivel.component.html'
})
export class HorarioDisponivelComponent implements OnInit, OnDestroy {
  horarioDisponivels?: IHorarioDisponivel[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected horarioDisponivelService: HorarioDisponivelService,
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
      this.horarioDisponivelService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IHorarioDisponivel[]>) => (this.horarioDisponivels = res.body ? res.body : []));
      return;
    }
    this.horarioDisponivelService.query().subscribe((res: HttpResponse<IHorarioDisponivel[]>) => {
      this.horarioDisponivels = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHorarioDisponivels();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHorarioDisponivel): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHorarioDisponivels(): void {
    this.eventSubscriber = this.eventManager.subscribe('horarioDisponivelListModification', () => this.loadAll());
  }

  delete(horarioDisponivel: IHorarioDisponivel): void {
    const modalRef = this.modalService.open(HorarioDisponivelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.horarioDisponivel = horarioDisponivel;
  }
}
