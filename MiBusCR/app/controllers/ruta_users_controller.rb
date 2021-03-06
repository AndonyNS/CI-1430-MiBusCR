class RutaUsersController < ApplicationController
  before_filter :restrict_access

  # GET /ruta_users
  # GET /ruta_users.json
  def index
    @ruta_users = @current_user.ruta_user
    render json: @ruta_users.as_json(only: [:ruta_id])
  end

  # GET /ruta_users/1
  # GET /ruta_users/1.json
  def show
    head :unauthorized
  end

  # GET /ruta_users/new
  def new
    @ruta_user = RutaUser.new
  end

  # GET /ruta_users/1/edit
  def edit
  end

  # POST /ruta_users
  # POST /ruta_users.json
  def create
    if params[:ruta_id] != ""
      @ruta_user = @current_user.ruta_user.find_by(ruta_id: params[:ruta_id])
      if @ruta_user.nil?
        @ruta = Ruta.find_by(id: params[:ruta_id])
        if !@ruta.nil?
          @ruta_user = RutaUser.new(ruta:@ruta, user:@current_user)
            if @ruta_user.save
              render json: @ruta_user.as_json(only: [:ruta_id])
            else
              render json: @ruta_user.errors, status: :unprocessable_entity
            end
        else
          render json: {message: 'Invalid ruta_id'}, status: :unprocessable_entity
        end
      end
    end
  end

  # PATCH/PUT /ruta_users/1
  # PATCH/PUT /ruta_users/1.json
  def update
  end

  # DELETE /ruta_users/1
  # DELETE /ruta_users/1.json
  def destroy
    @ruta_user = @current_user.ruta_user.find_by(ruta_id: params[:id])
    if !@ruta_user.nil?
      @ruta_user.destroy
    end
    head :no_content
  end
end
