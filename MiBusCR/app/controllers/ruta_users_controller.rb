class RutaUsersController < ApplicationController
  before_action :user_set
  before_action :authenticate_user
  before_action :set_ruta_user, only: [:show, :edit, :update, :destroy]

  # GET /ruta_users
  # GET /ruta_users.json
  def index
    @ruta_users = RutaUser.all
  end

  # GET /ruta_users/1
  # GET /ruta_users/1.json
  def show
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
    #@rutas_usuario = RutasUsuario.new(rutas_usuario_params)
    if current_user != nil
      respond_to do |format|
        if params[:ruta_favorita] != ""
          @ruta = Ruta.find(params[:ruta_favorita])
          @user = current_user
          @rutas_usuario = RutasUsuario.new(ruta:@ruta, user:@user)
        end
        if @rutas_usuario.save
          format.html { redirect_to @rutas_usuario, notice: 'Ruta favorita agregada.' }
          format.json { render :show, status: :created, location: @rutas_usuario }
        else
          format.html { render :new }
          format.json { render json: @rutas_usuario.errors, status: :unprocessable_entity }
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
    @ruta_user.destroy
    respond_to do |format|
      format.html { redirect_to ruta_users_url, notice: 'Ruta user was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_ruta_user
      @ruta_user = RutaUser.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def ruta_user_params
      params[:ruta_user]
    end
end
