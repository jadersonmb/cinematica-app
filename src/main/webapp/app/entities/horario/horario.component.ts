import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHorario } from 'app/shared/model/horario.model';
import { HorarioService } from './horario.service';
import { HorarioDeleteDialogComponent } from './horario-delete-dialog.component';

@Component({
  selector: 'jhi-horario',
  templateUrl: './horario.component.html'
})
export class HorarioComponent implements OnInit, OnDestroy {
  horarios?: IHorario[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected horarioService: HorarioService,
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
      this.horarioService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IHorario[]>) => (this.horarios = res.body ? res.body : []));
      return;
    }
    this.horarioService.query().subscribe((res: HttpResponse<IHorario[]>) => {
      this.horarios = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHorarios();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHorario): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHorarios(): void {
    this.eventSubscriber = this.eventManager.subscribe('horarioListModification', () => this.loadAll());
  }

  delete(horario: IHorario): void {
    const modalRef = this.modalService.open(HorarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.horario = horario;
  }
}
