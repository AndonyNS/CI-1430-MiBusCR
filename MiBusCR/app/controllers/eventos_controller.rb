class EventosController < ApplicationController
  before_action :set_ruta, only: [:show]
  before_filter :restrict_access

  # GET /eventos
  # GET /eventos.json
  def index
    @eventos = Evento.all

    render json: @eventos
  end

  # GET /eventos/1
  # GET /eventos/1.json
  def show
    @evento = Evento.find(params[:id])

    render json: @evento
  end

  # POST /eventos
  # POST /eventos.json
  def create
    head :unauthorized
  end

  # PATCH/PUT /eventos/1
  # PATCH/PUT /eventos/1.json
  def update
    head :unauthorized
  end

  # DELETE /eventos/1
  # DELETE /eventos/1.json
  def destroy
    head :unauthorized
  end

  private 
    def set_ruta
      begin  
        @evento = Evento.find(params[:id])
      rescue Exception => e  
        head 404
      end
    end
end
