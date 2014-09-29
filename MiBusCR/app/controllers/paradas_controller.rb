class ParadasController < ApplicationController
  before_action :set_parada, only: [:show]
  before_filter :restrict_access

  # GET /paradas
  # GET /paradas.json
  def index
    @paradas = Parada.all

    render json: @paradas.as_json(only: [:nombre, :techo, :latitud, :longitud ])
  end

  # GET /paradas/1
  # GET /paradas/1.json
  def show
    render json: @parada.as_json(only: [:nombre, :techo, :latitud, :longitud ])
  end

  # POST /paradas
  # POST /paradas.json
  def create
    head :unauthorized
  end

  # PATCH/PUT /paradas/1
  # PATCH/PUT /paradas/1.json
  def update
    head :unauthorized
  end

  # DELETE /paradas/1
  # DELETE /paradas/1.json
  def destroy
    head :unauthorized
  end

  private 
    # Never trust parameters from the scary internet, only allow the white list through. 
    def parada_params 
          params.permit(:nombre, :techo, :latitud, :longitud)
    end

    def set_parada
      begin  
        @parada = Parada.find(params[:id])
      rescue Exception => e  
        head 404
      end
    end
end
