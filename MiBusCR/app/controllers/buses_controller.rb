class BusesController < ApplicationController
  before_action :set_bus, only: [:show]

  # GET /buses
  # GET /buses.json
  def index
    @buses = Bus.all

    render json: @buses.as_json(only: [:id, :placa])
  end

  # GET /buses/1
  # GET /buses/1.json
  def show
    render json: @bus.as_json(only: [:id, :placa],include: [gps:{only: [:id_gps]}])
  end

  # POST /buses
  # POST /buses.json
  def create
    head :unauthorized
  end

  # PATCH/PUT /buses/1
  # PATCH/PUT /buses/1.json
  def update
    head :unauthorized
  end

  # DELETE /buses/1
  # DELETE /buses/1.json
  def destroy
    head :unauthorized
  end

  private
    # Never trust parameters from the scary internet, only allow the white list through.
    def bus_params
      params.require(:bus).permit(:placa)
    end

    def set_bus
      begin  
        @bus = Bus.find(params[:id])
      rescue Exception => e  
        head 404
      end
    end
end
